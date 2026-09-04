import React, { useState, useEffect } from 'react'
import {
  Users,
  UserPlus,
  Mail,
  MapPin,
  User,
  RefreshCw,
  Server,
  CheckCircle2,
  AlertCircle,
  Clock,
  Sparkles,
  ExternalLink
} from 'lucide-react'

// Dynamically resolve backend API base URL based on browser's current hostname or environment variable
const getApiBaseUrl = () => {
  const envUrl = import.meta.env.VITE_API_BASE_URL
  if (envUrl && !envUrl.includes('localhost') && !envUrl.includes('127.0.0.1')) {
    return envUrl
  }
  const hostname = typeof window !== 'undefined' && window.location.hostname ? window.location.hostname : 'localhost'
  return `http://${hostname}:8089/api/users`
}

const API_BASE_URL = getApiBaseUrl()


export default function App() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [serverOnline, setServerOnline] = useState(null)
  const [statusMessage, setStatusMessage] = useState(null) // { type: 'success' | 'error', text: '' }

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    address: ''
  })

  // Fetch all users
  const fetchUsers = async () => {
    setLoading(true)
    try {
      const response = await fetch(API_BASE_URL)
      if (!response.ok) {
        throw new Error(`HTTP Error: ${response.status}`)
      }
      const data = await response.json()
      setUsers(Array.isArray(data) ? data : [])
      setServerOnline(true)
    } catch (err) {
      console.error('Failed to fetch users:', err)
      setServerOnline(false)
    } finally {
      setLoading(false)
    }
  }

  // Initial fetch on component mount
  useEffect(() => {
    fetchUsers()
  }, [])

  // Form input change handler
  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }))
  }

  // Form submission handler
  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!formData.name.trim() || !formData.email.trim() || !formData.address.trim()) {
      setStatusMessage({
        type: 'error',
        text: 'Please fill out all required fields (Name, Email, Address).'
      })
      return
    }

    setSubmitting(true)
    setStatusMessage(null)

    try {
      const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: formData.name.trim(),
          email: formData.email.trim(),
          address: formData.address.trim()
        })
      })

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}))
        throw new Error(errorData.error || `Server returned ${response.status}`)
      }

      const createdUser = await response.json()
      setUsers((prev) => [createdUser, ...prev.filter(u => u.id !== createdUser.id)])
      setFormData({ name: '', email: '', address: '' })
      setStatusMessage({
        type: 'success',
        text: `Customer "${createdUser.name}" added successfully!`
      })
      setServerOnline(true)
    } catch (err) {
      console.error('Error submitting form:', err)
      setStatusMessage({
        type: 'error',
        text: `Failed to add user: ${err.message}`
      })
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="app-container">
      {/* Background Glow Blobs */}
      <div className="bg-glow blob-1"></div>
      <div className="bg-glow blob-2"></div>

      <div className="content-wrapper">
        {/* Header Section */}
        <header className="header">
          <div className="brand-badge">
            <span className="brand-icon">🍔</span>
            <span className="brand-name">FoodDelivery</span>
            <span className="env-pill">CI/CD Demo</span>
          </div>

          <div className="status-bar">
            <div className={`status-indicator ${serverOnline === true ? 'online' : serverOnline === false ? 'offline' : 'checking'}`}>
              <span className="status-dot"></span>
              <span className="status-text">
                {serverOnline === true ? 'Backend Live' : serverOnline === false ? 'Backend Offline' : 'Connecting...'}
              </span>
            </div>
            <span className="api-endpoint-label">
              <Server size={14} />
              <code>{API_BASE_URL}</code>
            </span>
          </div>
        </header>

        {/* Hero Section */}
        <div className="hero-banner">
          <h1>Customer Management & CI/CD Verification</h1>
          <p>
            Minimal full-stack testbed verifying automated packaging, multi-stage Docker builds, and Jenkins deployment pipelines.
          </p>
        </div>

        {/* Main Grid */}
        <div className="main-grid">
          {/* Left Column: Form */}
          <div className="card form-card">
            <div className="card-header">
              <div className="card-title">
                <UserPlus className="icon-accent" size={22} />
                <h2>Register Customer</h2>
              </div>
              <span className="subtitle">Add a customer record to PostgreSQL</span>
            </div>

            {statusMessage && (
              <div className={`alert-banner ${statusMessage.type}`}>
                {statusMessage.type === 'success' ? (
                  <CheckCircle2 size={18} />
                ) : (
                  <AlertCircle size={18} />
                )}
                <span>{statusMessage.text}</span>
              </div>
            )}

            <form onSubmit={handleSubmit} className="customer-form">
              <div className="form-group">
                <label htmlFor="name">
                  <User size={16} /> Full Name
                </label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  placeholder="e.g. John Doe"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">
                  <Mail size={16} /> Email Address
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  placeholder="e.g. john.doe@example.com"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="address">
                  <MapPin size={16} /> Delivery Address
                </label>
                <textarea
                  id="address"
                  name="address"
                  rows="3"
                  placeholder="e.g. 123 Main Street, Suite 4B, Metro City"
                  value={formData.address}
                  onChange={handleInputChange}
                  required
                ></textarea>
              </div>

              <button
                type="submit"
                className="btn btn-primary"
                disabled={submitting}
                id="submit-customer-btn"
              >
                {submitting ? (
                  <>
                    <RefreshCw className="spin" size={18} />
                    <span>Saving to Database...</span>
                  </>
                ) : (
                  <>
                    <UserPlus size={18} />
                    <span>Add Customer</span>
                  </>
                )}
              </button>
            </form>
          </div>

          {/* Right Column: User List */}
          <div className="card list-card">
            <div className="card-header list-header">
              <div className="card-title">
                <Users className="icon-accent" size={22} />
                <h2>Customer Directory</h2>
                <span className="badge-counter">{users.length}</span>
              </div>
              <button
                onClick={fetchUsers}
                className="btn-icon"
                title="Refresh Customer List"
                disabled={loading}
              >
                <RefreshCw className={loading ? 'spin' : ''} size={18} />
              </button>
            </div>

            <div className="list-content">
              {loading && users.length === 0 ? (
                <div className="empty-state">
                  <RefreshCw className="spin icon-muted" size={32} />
                  <p>Loading customers from PostgreSQL...</p>
                </div>
              ) : users.length === 0 ? (
                <div className="empty-state">
                  <Users className="icon-muted" size={40} />
                  <h3>No customers yet</h3>
                  <p>Fill out the form on the left to add your first customer record.</p>
                </div>
              ) : (
                <div className="table-wrapper">
                  <table className="custom-table">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Address</th>
                      </tr>
                    </thead>
                    <tbody>
                      {users.map((user) => (
                        <tr key={user.id || user.email}>
                          <td className="id-cell">
                            <span className="id-tag">#{user.id}</span>
                          </td>
                          <td className="name-cell">
                            <strong>{user.name}</strong>
                          </td>
                          <td className="email-cell">
                            <span className="cell-with-icon">
                              <Mail size={14} />
                              {user.email}
                            </span>
                          </td>
                          <td className="address-cell">
                            <span className="cell-with-icon">
                              <MapPin size={14} />
                              {user.address}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* System Architecture Overview Bar */}
        <footer className="footer">
          <div className="info-chips">
            <div className="chip">
              <span className="chip-label">Frontend</span>
              <span className="chip-val">Port 3001 (React + Vite)</span>
            </div>
            <div className="chip">
              <span className="chip-label">Backend</span>
              <span className="chip-val">Port 8089 (Spring Boot 3)</span>
            </div>
            <div className="chip">
              <span className="chip-label">Database</span>
              <span className="chip-val">Port 3307 (PostgreSQL 16)</span>
            </div>
            <div className="chip">
              <span className="chip-label">Pipeline</span>
              <span className="chip-val">Jenkins CI/CD + Docker</span>
            </div>
          </div>
          <div className="footer-credits">
            FoodDelivery CI/CD Infrastructure Test Bed • Built with Spring Boot 3 & React
          </div>
        </footer>
      </div>
    </div>
  )
}
